#!/usr/bin/ruby

require 'rubygems'
require 'eventmachine'
require 'em-http'

module Server
  def post_init
    puts "Received a new connection"
    timer = EM::PeriodicTimer.new(5) do
      http_call = EM::HttpRequest.new("http://www.google.com/message/#{@user_id}").get
      http_call.callback do |http|  
        puts http.response
        puts "Sending data to client ..."
        send_data http.response   
    end  
   end
  end

  def receive_data(data)
    puts "User ID received #{data}"
    @user_id = data
  end
end

EM.run do 
  EM.start_server '0.0.0.0', 3001, Server
end
