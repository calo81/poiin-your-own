require 'net/http'
require 'net/https'

http = Net::HTTP.new('localhost', 3000)
http.use_ssl = false
path = '/poiin'


# POST request -> logging in
data = "{user_id:123,latitude:123.00,longitude:10.00}"
headers = {
  'Content-Type' => 'application/json'
}

resp, data = http.post(path, data, headers)


# Output on the screen -> we should get either a 302 redirect (after a successful login) or an error page
puts 'Code = ' + resp.code
puts 'Message = ' + resp.message
resp.each {|key, val| puts key + ' = ' + val}
puts data