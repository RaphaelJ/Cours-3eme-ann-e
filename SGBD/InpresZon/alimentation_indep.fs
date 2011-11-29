open System
open System.Net
open System.Text.RegularExpressions

type MediaSource =
    { Listing: Printf.StringFormat<int -> string>;
      PageExpr: string;
      ArticleExpr: string }

let books = {
      Listing="http://livre.fnac.com/l898/Meilleures-ventes-Livre?sl=-1.0\
&PageIndex=%d"
    ; PageExpr="http://livre.fnac.com/l898/Meilleures-ventes-Livre\\?SID=.{36}\
&UID=.{37}&Origin=FnacAff&OrderInSession=0&TTL=\\d+&sl=-1.0&PageIndex=(\\d+)\
&ItemPerPage=15"
    ; ArticleExpr="http://livre.fnac.com/a\d+/" }

let musics = {
      Listing="http://musique.fnac.com/l827/Meilleures-ventes-musique?sl=-1.0\
&PageIndex=%d"
    ; PageExpr="http://musique.fnac.com/l827/Meilleures-ventes-musique\\?SID\
=.{36}&UID=.{37}&Origin=FnacAff&OrderInSession=0&TTL=\\d+&sl=-1.0&PageIndex=\
(\\d+)&ItemPerPage=15"
    ; ArticleExpr="http://musique.fnac.com/a\d+/" }

let movies = {
      Listing="http://video.fnac.com/l6484/Meilleures-ventes-DVD-et-Blu-Ray\
?sl=-1.0&PageIndex=%d"
    ; PageExpr="http://video.fnac.com/l6484/Meilleures-ventes-DVD-et-Blu-Ray\
\\?SID=.{36}&UID=.{37}&Origin=FnacAff&OrderInSession=0&TTL=\\d+&sl=-1.0\
&PageIndex=(\\d+)&ItemPerPage=15"
    ;  ArticleExpr="http://video.fnac.com/a\d+/" }

/// Télécharge le contenu d'une URL
let downloadString (url: String) = (new WebClient()).DownloadString(url)

/// Extrait les liens vers les pages des articles les plus populaires
let extractListing media =
    let downloadListing iPage = sprintf media.Listing iPage |> downloadString

    // Récupère le nombre de pages de listing
    let firstPage = downloadListing 1
    let pagesMatches = Regex.Matches(firstPage, media.PageExpr)
    let nPages = seq { for m in pagesMatches-> Int32.Parse(m.Groups.[1].Value) }
                 |> Seq.max

    // Récupère le contenu de toutes les pages de listing
    let pagesContents =
        seq { for p in seq {2..nPages} -> downloadListing p }
        |> Seq.append (Seq.singleton firstPage)

    // Récupère les liens vers les articles du listing
    let articlesMatches content = Regex.Matches(content, media.ArticleExpr)
    let articlesLinks content =
        seq { for a in articlesMatches content -> a.Value }

    pagesContents |> Seq.map articlesLinks |> Seq.concat |> Set.ofSeq

// Extrait les informations d'une fiche HTML sur un média
let extractInformation url =
    let titleExpr =
        "<strong class=\"titre dispeblock\">\\s*([\\p{L}\\s\\p{P}\\d\+-]+)\
[<>&;\"/=\\p{L}\\s\\p{P}\\d\+-]*</strong>"
    let imageExpr =
        "<img src=\"(.+)\" alt=\".+\" id=\"artzoomimg\"\\s?/>"
    let priceExpr =
        "<span class=\"price[\\p{L}\\s\\p{P}]*\">(\\d+(,\\d+)?)\
\\s*&nbsp;\\s*&euro;\\s*</span>"
    let scoreExpr = "Note des internautes:\\s*\
<img src=\"http://www4-fr\.fnac-static\.com/img/decos/etoiles/stars\\d+.png\" \
alt=\"Note moyenne des internautes :(\\d+(,\\d+)?)/5\"/>"
    let infoExpr =
        "<tr>\\s*<th scope=\"row\"( align=\"left\")?><span>\
([\\p{L}\\s\\p{P}\\d\\+-]+)</span></th>\\s*<td><span>\\s*(<A HREF=\"(.+)\">)?\
([\\p{L}\\s\\p{P}\\d\\+-]+)(</A>)?\\s*(</span></td>)?\\s*</tr>"

    let content = downloadString url

    let title = Regex.Match(content, titleExpr).Groups.[1].Value.Trim()

    let imageUrl = Regex.Match(content, imageExpr).Groups.[1].Value.Trim()
    let image = imageUrl

    let price = Double.Parse(Regex.Match(content, priceExpr).Groups.[1].Value)

    let scoreMatch = Regex.Match(content, scoreExpr)
    let score = if scoreMatch.Success
                then Some (Double.Parse(scoreMatch.Groups.[1].Value))
                else None

    let info = [ for m in Regex.Matches(content, infoExpr) ->
                 (m.Groups.[2].Value.Trim(), m.Groups.[5].Value.Trim()) ]

    (title, image, price, score, Map.ofList info)

let displayArticle url (title, img, price, score, is) =
    printfn "\nTitre: %s (%s) Prix: %.2f€ Score: %O" title url price score
    printfn "Image: %s" img
    for KeyValue(k, v) in is do
        printfn "%s:\n\t %s" k v
    printfn ""


do let articles = extractListing books
                  |> Set.union (extractListing musics)
                  |> Set.union (extractListing movies)

   printfn "%d articles à extraire" (articles |> Set.count)

   for url in articles do
        displayArticle url (extractInformation url)
            
   printfn "%d articles extraits" (articles |> Set.count)