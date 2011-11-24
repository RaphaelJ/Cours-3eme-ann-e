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

// Extrait les informations d'une fiche HTML d'un média
let extractInformation url =
    let titleExpr =
        "<strong class=\"titre dispeblock\">\\s*([\\w\\s\\p{P}]+)\\s*</strong>"
    let imageExpr =
        "<a href=\"(.+)\" class=\"activeimg\">"
    let priceExpr =
        "<span class=\"price\">(\\d,\\d+)\\s*€</span>"
    let infoExpr =
        "<tr>\\s*<th scope=\"row\" align=\"left\"><span>([\\w\\s\\p{P}]+)\
</span></th>\\s*<td><span>\\s*(<A HREF=\"(.+)\">)?([\\w\\s\\p{P}]+)(</A>)?\
\\s*</span></td>\\s*</tr>"

    let content = downloadString url

    let title = Regex.Match(content, titleExpr).Groups.[1].Value.Trim()
    let imageUrl = Regex.Match(content, imageExpr).Groups.[1].Value.Trim()
    let image = imageUrl
    printfn "'%s'" (Regex.Match(content, priceExpr).Groups.[1].Value)
    let price = Double.Parse(Regex.Match(content, priceExpr).Groups.[1].Value)
    let info = [ for m in Regex.Matches(content, infoExpr) ->
                 (m.Groups.[1].Value.Trim(), m.Groups.[4].Value.Trim()) ]

    (title, image, price, Map.ofList info)

do let articles = extractListing books
                  |> Set.union (extractListing musics)
                  |> Set.union (extractListing movies)
   for l in articles do
        let (t, i, p, _) = extractInformation l
        printfn "T: %s\nI: %s\nP: %f" t i p
   printfn "%d articles" (articles |> Set.count)
