#!/usr/bin/ruby

require 'rubygems'
require 'eventmachine'

module Server
  def post_init
    puts "Received a new connection"
    timer = EM::PeriodicTimer.new(5) do
      puts "Sending data to client ..."
      send_data "hola\n"
    end
  end

  def receive_data(data)
    puts data
    send_data("helo\n")
  end
end

EM.run do 
  EM.start_server '0.0.0.0', 3001, Server
end
