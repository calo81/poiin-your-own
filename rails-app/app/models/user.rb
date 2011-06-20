class User
  include MongoMapper::Document
  include ActiveModel::Serializers::JSON
  self.include_root_in_json = false
  key :messages, Array

  def self.exists?(user_id)
     if User.find(user_id)
       true
     else
       false
     end
  end

  def send_message(message)
    @messages ||= []
    @messages << message
    save!
  end
end