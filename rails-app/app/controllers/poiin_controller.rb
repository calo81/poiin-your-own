class PoiinController < ApplicationController
  def create
    unless User.exists?(params['user_id'])
      user = User.new "_id" => params['user_id'], "categories" => ['default']
      user.save
    end
    poiin = Poiin.new(params)
    poiin.save
    render :json  => {code: "ok"}
  end

  def index
    user = User.find_by_id(params['user_id'])
    poiins = Poiin.find_by_categories(user.categories)
    render :json => poiins
  end
end