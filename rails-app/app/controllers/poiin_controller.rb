class PoiinController < ApplicationController

  def save_user_if_needed
    unless User.exists?(params['user_id'])
      user = User.new "_id" => params['user_id'], "categories" => ['default'], "user_name"=>params['user_name']
      user.save
    end
  end

  def create
    save_user_if_needed()
    user = User.find(params['user_id'])
    poiin = Poiin.new "user_id"=>params["user_id"], "longitude"=>params["longitude"],"latitude"=>params["latitude"]
    poiin.categories = user.categories
    poiin.save
    render :json  => {code: "ok"}
  end

  def index
    user = User.find(params['user_id'].to_i)
    poiins = Poiin.where(:categories => user.categories).all
    to_return = build_return_object(poiins)
    render :json => to_return.to_json
  end

  def build_return_object(poiins)
    poiins.map do |poiin|
      {:poiin => poiin, :user=>User.find(poiin.user_id)}
    end
  end

  private :save_user_if_needed, :build_return_object
end