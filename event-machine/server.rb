#!/usr/bin/ruby

require 'rubygems'
require 'eventmachine'
require 'em-http'
require 'net/http'

module Server
  USER_MESSAGE_TYPE = 0
  POIIN_MESSAGE_TYPE = 1

  def post_init
    puts "Received a new connection"
    EM::PeriodicTimer.new(5) do
      poll_for_messages
    end

    EM::PeriodicTimer.new(30) do
      poll_for_poiins
    end

  end

  def poll_for_messages
    http = EventMachine::HttpRequest.new('http://127.0.0.1:3000/message').get :query => {'user_id' => @user_id}
    http.callback do
      if http.response_header.status == 200
        if http.response && http.response != "[]"
          puts "Message received #{http.response}. Sending to client.."
          send_data "#{USER_MESSAGE_TYPE}|#{http.response}\n"
        end
      end
    end
  end

  def poll_for_poiins
    http = EventMachine::HttpRequest.new('http://127.0.0.1:3000/poiin').get :query => {'user_id' => @user_id}
    http.callback do
      if http.response_header.status == 200
        if http.response && http.response != "[]"
          puts "Message received #{http.response}. Sending to client.."
          send_data "#{POIIN_MESSAGE_TYPE}|#{http.response}\n"
        end
      end
    end
  end

  def unbind
    puts " Connection terminated "
  end

  def receive_data(received)
    puts " User ID received #{received}"
    operation_data = received.split("|")
    case operation_data[0]
      when "USER_ID"
        @user_id = operation_data[1].chop
      else
        puts "Unrecognized client request"
    end
  end
end

EM.run do
  puts "Starting EventMachine Server ... "
  EM.start_server '0.0.0.0', 3001, Server
end
