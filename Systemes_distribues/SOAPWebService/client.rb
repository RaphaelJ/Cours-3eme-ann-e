require "savon"

wsdl_uri = "http://localhost:8080/WebServiceSOAP/WebServiceSOAP?wsdl"
user = "rapha"
pass = "pass"

client = Savon::Client.new do |wsdl, http, wsse|
    wsdl.document = "http://localhost:8080/WebServiceSOAP/WebServiceSOAP?wsdl"
    wsse.credentials user, pass
end

response = client.request :wsdl, :historique_vols

