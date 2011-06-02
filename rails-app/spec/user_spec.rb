require "spec_helper"

describe "User" do

  it "should return false from exists if not exists" do
      User.exists?("qweqwe").should be_false
  end

    it "should return true from exists if exists" do
      user = User.new :id => 123,:name =>"aaa"
      user.save
      User.exists?(123).should be_true
      user.delete
  end
end