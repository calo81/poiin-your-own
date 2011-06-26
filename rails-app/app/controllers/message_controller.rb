class MessageController   < ApplicationController
  require "mongo"
  require "bson"
  def create
    user_to = User.find(params["to"])
    user_to.send_message :id=>BSON::ObjectId.new, :message => params["message"], :from=>params["from"]
    render :json => {}
  end

  def index
    user = User.find(params["user_id"].to_i)
    render :json => user.messages
  end

  def destroy
    user = User.find(params["user_id"].to_i)
    user.remove_message(params[:id])
    render :json => {}
  end
end