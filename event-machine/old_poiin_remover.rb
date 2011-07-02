   require 'em-mongo'
   def ten_minutes_from_now
     minutes_from_now 1200
   end
   
   def twenty_minutes_from_now
     minutes_from_now 2400
   end
   
   def minutes_from_now(seconds)
     (Time.new.to_i - seconds) * 1000
   end
   
   EM.run do
     db = EM::Mongo::Connection.new('82.165.139.7', 27017).db('#poiin-development')
     EM::PeriodicTimer.new(10) do
       collection = db.collection('poiins')
       puts "Deleting all old poiins"
       collection.remove({"date" => {"$lt" => ten_minutes_from_now}})
     end
     
     EM::PeriodicTimer.new(10) do
       collection = db.collection('person_and_poiins')
       puts "Deleting all old user_id - poiins realtions"
       collection.remove({"date" => {"$lt" => twenty_minutes_from_now}})
     end
     
   end
