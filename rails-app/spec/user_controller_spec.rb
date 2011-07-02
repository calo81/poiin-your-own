require "spec_helper"

describe "UserController" do
  before(:each) do
    @controller = UserController.new
  end

  it "should be able to say if user DO exists" do
    @controller.params = {"id" => "3333"}
    User.should_receive(:exists?).with(3333).and_return true
    @controller.should_receive(:render).with({:json => "{registered:'true'}"})
    @controller.show
  end

  it "should be able to say if user NOT exists" do
    @controller.params = {"id" => "3333"}
    User.should_receive(:exists?).with(3333).and_return false
    @controller.should_receive(:render).with({:json => "{registered:'false'}"})
    @controller.show
  end

  it "should allo creation of users with their categories" do
    @controller.params={"id"=>111,"categories"=>["cat1","cat2"],"name"=>"carlo"}
    user=mock("user")
    User.should_receive(:new).with({"_id"=>111,"categories"=>["cat1","cat2"],"user_name"=>"carlo"}).and_return(user)
    user.should_receive(:save)
    @controller.create
  end
end