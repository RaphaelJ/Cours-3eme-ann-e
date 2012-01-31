require "savon"

debug = true
wsdl_uri = "http://localhost:8080/WebServiceSOAP/WebServiceSOAP?wsdl"
user = "rapha"
pass = "pass"

Savon.configure do |config|
  config.log = debug
end

client = Savon::Client.new do |wsdl, http, wsse|
    wsdl.document = wsdl_uri
    wsse.credentials user, pass
end

def afficher_vols(vols)
    puts "Reponse: "
    
    if vols != nil 
        vols.each do |vol|
            puts
            puts "Aeroport de depart: #{vol[:aeroport_depart]}"
            puts "Heure de depart: #{vol[:depart]}"
            puts "Aeroport d'arrivee: #{vol[:aeroport_arrivee]}"
            puts "Heure d'arrivee: #{vol[:arrivee]}"
            puts "Aeroport de depart: #{vol[:aeroport_depart]}"
            puts "ID pilote: #{vol[:pilote]}"
            puts "ID Avion: #{vol[:avion]}"
        end
    else
        puts "Aucun vol"
    end
end

puts "Historique des vols"
puts "-------------------"
afficher_vols client.request(:wsdl, :historique_vols)\
                     [:historique_vols_response][:vol]

puts "Vols programmes"
puts "---------------"
afficher_vols client.request(:wsdl, :prochains_vols)\
                     [:prochains_vols_response][:vol]

puts "Vols pour une date"
puts "------------------"
afficher_vols client.request(:wsdl, :vols, {
                        :date => Date.new(2013, 10, 10)
                    })[:vols_response][:vol]
