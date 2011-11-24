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

let downloadString (url: String) = (new WebClient()).DownloadString(url)

/// Extrait les liens vers les pages des articles les plus populaires
let extractListing media =
    let downloadListing iPage = sprintf media.Listing iPage |> downloadString
    let firstPage = downloadListing 1
    // Récupère le nombre de pages de listing
    let pagesMatches = Regex.Matches(firstPage, media.PageExpr)
    let nPages = seq { for m in pagesMatches-> Int32.Parse(m.Groups.[1].Value) }
                 |> Seq.max
    printfn "%d pages" nPages
    // Récupère le contenu de toutes les pages de listing
    let pagesContents =
        seq { for p in seq {2..nPages} -> downloadListing p }
        |> Seq.append (Seq.singleton firstPage)
    // Récupère les liens vers les articles du listing
    let articlesMatches content = Regex.Matches(content, media.ArticleExpr)
    let articlesLinks content =
        seq { for a in articlesMatches content -> a.Value }
    pagesContents |> Seq.map articlesLinks |> Seq.concat |> Set.ofSeq 

do let articles = extractListing books
                  |> Seq.append (extractListing musics)
                  |> Seq.append (extractListing movies)
   for l in articles do
        printfn "%s" l
   printfn "%d articles" (articles |> Seq.toList |> List.length)
