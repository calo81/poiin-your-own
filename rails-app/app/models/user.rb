class User
  include MongoMapper::Document
  include ActiveModel::Serializers::JSON
  self.include_root_in_json = false
end