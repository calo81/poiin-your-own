class User
  include MongoMapper::Document
  include ActiveModel::Serializers::JSON
  self.include_root_in_json = false

  def self.exists?(user_id)
     if User.find(user_id)
       true
     else
       false
     end
  end
end