require "spec_helper"

describe "MessageController" do
  before(:each) do
    @controller = MessageController.new
  end
  it "should receive messages and add to message queue" do
    @controller.params = {"message" => "El mensaje", "from" => 111111, "to" => 999999}
    user_receiving=mock("user")
    User.should_receive(:find).with(999999).and_return(user_receiving)
    BSON::ObjectId.should_receive(:new).and_return 111
    user_receiving.should_receive(:send_message).with({:id => 111,:message => "El mensaje", :from => 111111, :from_twitter_id=>nil, :from_facebook_id=>nil})
    @controller.should_receive(:render)
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


  it "should delete message on acknowledgement"  do
    @controller.params = {:id => 111111,"user_id"=> 4444}
    user = mock(user)
    User.should_receive(:find).with(4444).and_return user
    user.should_receive(:remove_message).with(111111)
    @controller.should_receive(:render)
    @controller.destroy
  end

end