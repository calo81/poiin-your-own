class MessageController   < ApplicationController
  def create
    user_to = User.find(params["to"])
    user_to.send_message :message => params["message"], :from=>params["from"]
  end

  def index
    user = User.find(params["user_id"])
    render :json => user.messages
  end
end