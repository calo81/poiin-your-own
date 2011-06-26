require "spec_helper"

describe "User" do

  it "should return false from exists if not exists" do
    User.exists?("qweqwe").should be_false
  end

  it "should return true from exists if exists" do
    user = User.new :id => 123, :name =>"aaa"
    user.save
    User.exists?(123).should be_true
    user.delete
  end

  it "should store messages in message array when sending messages" do
    user = User.new :id => 123, :name =>"aaa"
    user.save
    user.send_message({:from=>"123123",:message=>"hola hola"})
    user.send_message({:from=>"999999",:message=>"ciao ciao"})
    user = User.find(123)
    user.messages.should == [{"from"=>"123123","message"=>"hola hola"},{"from"=>"999999","message"=>"ciao ciao"}]
  end

  it "should allow deletion of messages per id" do
    user = User.new :id => 123, :name =>"aaa"
    user.save
    user.send_message({"id" => 111,:from=>"123123",:message=>"hola hola"})
    user.send_message({"id" => 222,:from=>"999999",:message=>"ciao ciao"})
    user.remove_message("111")
    retrieved_user = User.find(123)
    retrieved_user.messages.should_not include({"id" => 111,"from"=>"123123","message"=>"hola hola"})
    retrieved_user.messages.should include({"id" => 222,"from"=>"999999","message"=>"ciao ciao"})
  end
end