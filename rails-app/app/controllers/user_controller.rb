class UserController    < ApplicationController
  def show
    if User.exists?(params["id"].to_i)
      render :json => "{registered:'true'}"
    else
      render :json => "{registered:'false'}"
    end
  end
end