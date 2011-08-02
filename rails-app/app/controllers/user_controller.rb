class UserController    < ApplicationController
  def show
    if User.exists?(params["id"].to_i)
      user = User.find(params["id"].to_i)
      render :json => "{registered:'true',twitter_id:#{user.twitter_id},facebook_id:#{user.facebook_id}}"
    else
      render :json => "{registered:'false'}"
    end
  end

  def create
    user = User.new "_id"=>params["id"].to_i, "categories"=>params["categories"], "user_name"=>params["name"], "twitter_id"=>params["twitter_id"], "facebook_id" => params["facebook_id"]
    user.save
  end

  def update
    user = User.find(params["id"].to_i)
    user.twitter_id = params["twitter_id"]
    user.facebook_id = params["facebook_id"]
    user.save
  end
end