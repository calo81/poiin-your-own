require "spec_helper"

describe "UserController" do
  before(:each) do
    @controller = UserController.new
  end

  it "should be able to say if user DO exists" do
    @controller.params = {"id" => "3333"}
    User.should_receive(:exists?).with(3333).and_return true
    user = mock("user")
    User.should_receive(:find).with(3333).and_return user
    user.should_receive(:twitter_id).and_return(1)
    user.should_receive(:facebook_id).and_return(2)
    @controller.should_receive(:render).with({:json => "{registered:'true',twitter_id:1,facebook_id:2}"})
    @controller.show
  end

  it "should be able to say if user NOT exists" do
    @controller.params = {"id" => "3333"}
    User.should_receive(:exists?).with(3333).and_return false
    @controller.should_receive(:render).with({:json => "{registered:'false'}"})
    @controller.show
  end

  it "should allow creation of users with their categories" do
    @controller.params={"id"=>111, "categories"=>["cat1", "cat2"], "name"=>"carlo"}
    user=mock("user")
    User.should_receive(:new).with({"_id"=>111, "categories"=>["cat1", "cat2"], "user_name"=>"carlo", "twitter_id"=>nil, "facebook_id"=>nil}).and_return(user)
    user.should_receive(:save)
    @controller.create
  end

  it "Should allow updating users" do
    @controller.params={"id"=>111, "twitter_id"=>111, "facebook_id"=>2323}
    user = mock("user")
    User.should_receive(:find).with(111).and_return(user)
    user.should_receive(:twitter_id=).with(111)
    user.should_receive(:facebook_id=).with(2323)
    user.should_receive(:save)
    @controller.update
  end
end