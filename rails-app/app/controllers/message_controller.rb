class MessageController   < ApplicationController
  require "mongo"
  require "bson"
  def create
    user_to = User.find(params["to"])
    user_to.send_message :id=>BSON::ObjectId.new, :message => params["message"], :from=>params["from"], :from_twitter_id => params["from_twitter_id"], :from_facebook_id => params["from_facebook_id"]
    render :json => {}
  end

  def index
    user = User.find(params["user_id"].to_i)
    messages = user.messages
    user.messages = []
    user.save
    render :json => messages
  end

  def destroy
    user = User.find(params["user_id"].to_i)
    user.remove_message(params[:id])
    render :json => {}
  end
end