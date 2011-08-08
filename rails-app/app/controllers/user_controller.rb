class UserController < ApplicationController


  def show
    if User.exists?(params["id"].to_i)
      user = User.find(params["id"].to_i)
      facebook_id = get_user_id(user,'facebook_id')
      twitter_id = get_user_id(user,'twitter_id')
      render :json => "{registered:'true'#{twitter_id}#{facebook_id}}"
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

  private
  def get_user_id(user,id_to_search)
    if user.send(id_to_search)
      return  ",#{id_to_search}:#{user.send(id_to_search)}"
    else
      return nil
    end
  end
end