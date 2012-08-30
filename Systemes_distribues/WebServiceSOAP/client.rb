require "savon"

debug = false
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

def afficher_vol(vol)   
    puts "Aeroport de depart: #{vol[:aeroport_depart]}"
    puts "Heure de depart: #{vol[:depart]}"
    puts "Aeroport d'arrivee: #{vol[:aeroport_arrivee]}"
    puts "Heure d'arrivee: #{vol[:arrivee]}"
    puts "ID pilote: #{vol[:pilote]}"
    puts "ID Avion: #{vol[:avion]}"
end

def afficher_vols(vols)
    puts "- - - - - - -"
    puts "- Reponse : -"
    puts "- - - - - - -"
    
    if vols != nil
        if vols.kind_of?(Array)
            vols.each afficher_vol
        else
            afficher_vol vols
        end
    else
        puts "Aucun vol"
    end
end

puts "Historique des vols"
puts "-------------------"
afficher_vols client.request(:wsdl, :historique_vols)\
                     [:historique_vols_response][:vol]

puts
puts "-------------------------------------------------------------------------"
puts

puts "Vols programmes"
puts "---------------"
afficher_vols client.request(:wsdl, :prochains_vols)\
                    [:prochains_vols_response][:vol]

puts
puts "-------------------------------------------------------------------------"
puts ""

puts "Vols pour une date"
puts "------------------"
afficher_vols client.request(:wsdl, :vols, {
                        :date => Date.new(2011, 12, 10)
                    })[:vols_response][:vol]
