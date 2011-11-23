open System
open System.Net
open System.Text.RegularExpressions

let (fnac_livres:Printf.StringFormat<_>) = "http://livre.fnac.com/l898/Meilleur\
es-ventes-Livre?sl=-1.0&PageIndex=%d"

let DownloadString (url:String) = (new WebClient()).DownloadString(url)
let ExtractLinks content =
    let expr =
        "http://livre.fnac.com/l898/Meilleures-ventes-Livre\?SID=.{36}\
&UID=.{37}&Origin=FnacAff&OrderInSession=0&TTL=\d+&sl=-1.0&PageIndex=(\d+)\
&ItemPerPage=15"
    
    for m in Regex.Matches(content, expr) do
        printfn "%s" (m.Groups.[1].Value)

    Regex.Matches(content, expr)

do printfn "Count: %d" (sprintf fnac_livres 1 |> DownloadString |> ExtractLinks).Count
