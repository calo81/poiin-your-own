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

  def remove_message(id)
    messages.delete_if {|message| id == message["id"].to_s}
    save!
  end
end