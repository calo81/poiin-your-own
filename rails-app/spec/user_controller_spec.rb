require "spec_helper"

describe "UserController" do
  before(:each) do
    @controller = UserController.new
  end

  it "should be able to say if user DO exists" do
    @controller.params = {"id" => "3333"}
    User.should_receive(:exists?).with("3333").and_return true
    @controller.should_receive(:render).with({:json => "{registered:'true'}"})
    @controller.show
  end

  it "should be able to say if user NOT exists" do
    @controller.params = {"id" => "3333"}
    User.should_receive(:exists?).with("3333").and_return false
    @controller.should_receive(:render).with({:json => "{registered:'false'}"})
    @controller.show
  end
end