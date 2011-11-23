open System
open System.Net
open System.Text.RegularExpressions

let (fnac_livres:Printf.StringFormat<_>) = "http://livre.fnac.com/l898/Meilleures-ventes-Livre?sl=-1.0&PageIndex=%d"

let DownloadString (url:String) = (new WebClient()).DownloadString(url)
let ExtractLinks content =
    let expr =
        "http://livre.fnac.com/l898/Meilleures-ventes-Livre?SID="
    for n in Regex.Matches(content, expr) do
        printfn "%s" content.[n.Index..n.Index+350]

do Console.WriteLine(sprintf fnac_livres 1 |> DownloadString |> ExtractLinks)

