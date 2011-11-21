open System
open Control.WebExtensions

let (fnac_livres:Printf.StringFormat<_>) = "http://livre.fnac.com/l898/Meilleures-ventes-Livre?sl=-1.0&PageIndex=%d"

let DownloadString = new WebClient().DownloadString

do Console.WriteLine(sprintf fnac_livres 1 |> DownloadString)