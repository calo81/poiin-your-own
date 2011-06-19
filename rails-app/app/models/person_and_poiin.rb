class PersonAndPoiin
   include MongoMapper::Document
   include ActiveModel::Serializers::JSON
   self.include_root_in_json = false
   attr_reader :person, :poiin

   def self.build_list(lis_of_poiins)
       lis_of_poiins.map do |poiin|
         user = User.find(poiin.user_id)
         PersonAndPoiin.new(user,poiin)
       end
   end

   def initialize(person,poiin)
    @person=person
    @poiin=poiin
  end

end