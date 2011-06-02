class Poiin
  include MongoMapper::Document
  include ActiveModel::Serializers::JSON
  self.include_root_in_json = false

  key :categories, Array

end