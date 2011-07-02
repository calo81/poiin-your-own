class PersonAndPoiin
   include MongoMapper::Document
   include ActiveModel::Serializers::JSON
   self.include_root_in_json = false

  def self.remove_old_poiins(user_id,poiin_id)
     half_hour_before_now  = (Time.now.to_i - 1800) * 1000
     delete_all({"user_id"=>user_id,"poiin_id"=>poiin_id,"date"=>{"$lt"=>half_hour_before_now}})
  end

  def self.filter_poiins(user_id,poiins)
    poiins.delete_if do |poiin|
       all({"user_id" => user_id,"poiin_id" => poiin.id}) != []
    end
  end

  def self.save(user_id,poiins)
    poiins.each do|poiin|
      PersonAndPoiin.new("user_id"=>user_id,"poiin_id"=>poiin.id,"date"=>Time.new.to_i).save
    end
  end
end