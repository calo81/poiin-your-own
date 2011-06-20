require "spec_helper"

describe "MessageController" do
  before(:each) do
    @controller = MessageController.new
  end
  it "should receive messages and add to message queue" do
    @controller.params = {"message" => "El mensaje", "from" => 111111, "to" => 999999}
    user_receiving=mock("user")
    User.should_receive(:find).with(999999).and_return(user_receiving)
    user_receiving.should_receive(:send_message).with({:message => "El mensaje", :from => 111111})
    @controller.create
  end

  it "should return all messages saved" do
    @controller.params = {"user_id" => 999999}
    user_receiving=mock("user")
    User.should_receive(:find).with(999999).and_return(user_receiving)
    user_receiving.should_receive(:messages).and_return([{"from"=> "me","message"=>"hola"}])
    @controller.should_receive(:render)
    @controller.index
  end


end