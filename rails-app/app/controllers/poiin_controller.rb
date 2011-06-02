class PoiinController < ApplicationController

  def save_user_if_needed
    unless User.exists?(params['user_id'])
      user = User.new "_id" => params['user_id'], "categories" => ['default']
      user.save
    end
  end

  def create
    save_user_if_needed()
    user = User.find(params['user_id'])
    poiin = Poiin.new(params)
    poiin.categories = user.categories
    poiin.save
    render :json  => {code: "ok"}
  end

  def index
    user = User.find(params['user_id'].to_i)
    poiins = Poiin.where(:categories => user.categories).all
    render :json => {:poiins => poiins}
  end

  private :save_user_if_needed
end