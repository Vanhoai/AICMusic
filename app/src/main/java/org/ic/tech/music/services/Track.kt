package org.ic.tech.music.services

data class Track(
    var id: String,
    var audioUrl: String,
    var teaserUrl: String,
    var title: String,
    var artistName: String,
    var duration: String,
)

val songs = listOf(
    Track(
        "1",
        "https://www.matb3aa.com/music/Wegz/Dorak.Gai-Wegz-MaTb3aa.Com.mp3",
        "https://angartwork.anghcdn.co/?id=105597079&size=640",
        "Track 1",
        "Wegz",
        "4:18"
    ),
    Track(
        "2",
        "https://mp3songs.nghmat.com/mp3_songs_Js54w1/CairoKee/Nghmat.Com_Cairokee_Marboot.B.Astek.mp3",
        "https://i.scdn.co/image/ab6761610000e5eb031d0209d9cb8abbc0505769",
        "Marboot B Astek",
        "Cairokee",
        "3:48"
    ),
    Track(
        "3",
        "https://www.matb3aa.com/music/Marwan-Pablo/Album-CTRL-2021/GHABA-MARWAN.PABLO-MaTb3aa.Com.mp3",
        "https://lastfm.freetls.fastly.net/i/u/770x0/5ac22055e70c20939ae60b4825c8b04b.jpg",
        "GHABA",
        "Marwan Pablo",
        "3:02"
    ),
    Track(
        "4",
        "https://www.matb3aa.com/music/Wegz/ATm-Wegz-MaTb3aa.Com.mp3",
        "https://www.qalimat.com/wp-content/uploads/2020/07/%D9%88%D9%8A%D8%AC%D8%B2.jpg",
        "ATM",
        "Wegz",
        "3:02"
    ),
    Track(
        "5",
        "https://mp3songs.nghmat.com/mp3_songs_Js54w1/Sharmoofers/Nghmat.Com_Sharmoofers_Moftked.El.Habeba.mp3",
        "https://aghanyna.net/en/wp-content/uploads/2017/04/sharmoofers-2017.jpeg",
        "Moftked El Habeba",
        "Sharmoofers",
        "3:21"
    ),
    Track(
        "6",
        "https://www.matb3aa.com/music/Shahyn/Ma.3aleena-Shahyn-MaTb3aa.Com.mp3",
        "https://i.scdn.co/image/ab6761610000e5eb368ee15b276f33ab10530737",
        "Ma Aleena",
        "Shahyn",
        "3:27"
    ),
)