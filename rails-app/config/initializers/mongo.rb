MongoMapper.connection = Mongo::Connection.new('82.165.139.7', 27017)
MongoMapper.database = "#poiin-#{Rails.env}"

if defined?(PhusionPassenger)
   PhusionPassenger.on_event(:starting_worker_process) do |forked|
     MongoMapper.connection.connect if forked
   end
end