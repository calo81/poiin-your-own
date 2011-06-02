require "spec_helper"

describe "PoiinController" do
  context "On sending poiins" do
    before(:each) do
      @controller = PoiinController.new
      @controller.params = {"user_id" => "123", "latitude" => "100.33", "longitude" => "30.00"}
    end

    it "should call poiin storing with the poiin for existent user" do
      User.should_receive(:exists?).with("123").and_return(true)
      user = mock("user")

      User.should_receive(:find).with("123").and_return(user)
      user.should_receive(:categories).and_return(["cat1","cat2"])
      poiin = mock("poiin")
      Poiin.should_receive(:new).with({"user_id" => "123", "latitude" => "100.33", "longitude" => "30.00"}).and_return(poiin)
      poiin.should_receive(:categories=).with(["cat1","cat2"])
      poiin.should_receive(:save)
      @controller.should_receive(:render)
      @controller.create
    end

    it "should save a user and then poiin if user doesn't exist" do
      User.should_receive(:exists?).with("123").and_return(false)
      user = mock("user")
      poiin = mock("poiin")
      User.should_receive(:new).with({"_id" => "123", "categories" => ["default"]}).and_return(user)
      user.should_receive(:save)

      retrieved_user = mock("retrieved_user")
      User.should_receive(:find).with("123").and_return(retrieved_user)
      retrieved_user.should_receive(:categories).and_return(["cat1","cat2"])
      Poiin.should_receive(:new).with({"user_id" => "123", "latitude" => "100.33", "longitude" => "30.00"}).and_return(poiin)
      poiin.should_receive(:categories=).with(["cat1","cat2"])
      poiin.should_receive(:save)
      @controller.should_receive(:render)
      @controller.create
    end
  end

  context "On retrieving poiins" do
    it "should call to retrieve the poiins of the category of the user requesting" do
      @controller = PoiinController.new
      @controller.params = {"user_id" => "123"}
      user = mock("user")
      User.should_receive(:find).with(123).and_return(user)
      user.should_receive(:categories).and_return(["category1"])
      mock_plucky = mock("plucky")
      Poiin.should_receive(:where).with(:categories=>["category1"]).and_return(mock_plucky)
      mock_plucky.should_receive(:all)
      @controller.should_receive(:render)
      @controller.index
    end
  end

end