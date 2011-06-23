#!/usr/bin/ruby

require 'rubygems'
require 'eventmachine'
require 'em-http'
require 'net/http'

module Server
  def post_init
    puts "Received a new connection"
    timer = EM::PeriodicTimer.new(5) do         
      http = EventMachine::HttpRequest.new('http://127.0.0.1:3000/message').get :query => {'user_id' => @user_id}
      http.callback do
        if http.response_header.status == 200
          send_data http.response
        end
      end
    end
  end

  def receive_data(data)
    puts "User ID received #{data}"
    @user_id = data.chop
  end
end

EM.run do 
  EM.start_server '0.0.0.0', 3001, Server
end
