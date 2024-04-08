# Api Plugin
Diese Plugin fügt
- Vanish Funktionen
- Ränge
- GUIs
- Chat
- Fanta-Mod
- Placeholder
- Tablist

### Vanish
Es gibt einen neuen Command "vanish", dieser fügt mehrere Funktionen wie
- vanish on
- vanish off
- vanish logout
- vanish login
- vanish hide
- vanish show
- vanish list
- vanish info
- vanish items

hinzu

#### - vanish on 
Dieser befehl vanish dich oder den Spieler der nach dem "on" steht.
Als Vanish Player wirst du vor anderen Spielen versteckt. Dich können aber immer noch Spieler sehen die ein 
höheres oder gleich größes See Level haben als dein Use Level. Solltest du versuchen eine Chatnachricht 
zusenden wird diese nicht gesendet, außer deine Nachricht beginnt mit '#'. Da weil du gevanished bis greifen
dich keine Monster an, du bekommst keinen Schaden und Hunger.

#### - vanish off
Entvanished dich und zeigt dich wieder allen Spielern.

#### - vanish logout
Sendet eine Fake Logout Nachricht an alle Spieler.

#### - vanish login
Sendet eine Fake Login Nachricht an alle Spieler.

#### - vanish hide
Versteckt den Spieler an dritter Stelle vor dir oder den Spieler and dritter Stelle vom Spieler an der 
vierten Position

#### - vanish show
Das selbe wie vanish hide nur halt das die Spieler gezeigt werden egal ob sie gevanished sind oder nicht.

#### - vanish list
Listet alle vanish spieler auf.

#### - vanish info
Gibt darüber info welche Spieler dich sehen können.

#### - vanish item
Toggelt ob du items aufheben kannst (standart kannst du keine Items aufheben).

#### - vanish
Toggelt deinen vanish status.

#### Sonstiges
Spieler mit der Berechtigung "cmd.vanish.vanishonjoin" werden wenn sie den Server beitreten gevanished.

### Ränge
In der rang.yml datei werden die Ränge definiert diese Ränge werden durch die permission "rang.<rangID<r>>"
sollte ein Spieler mehrere dieser Berechtigungen haben wird der Rang mit der höhsten Prioritiät genommen.
Der Rang wird in der TabListe, im Chat, beim betreten und verlassen des Server angezeigt.

### GUI
Wenn du einen Spieler Rechtsklickst und die Berechtigung "player.data.rightclick" hast öffnet sich ein 
Inventar wo man sachen einstellen kann wie Gamemode, Kick, Ban, Kill.

### Chat
Das Plugin bearbeitet den Chat die Nachrichten werden nach dem Rang formatiert.

#### HoverEvent
Ein Spieler mit der Berechtigung "chat.info" sieht, wenn er mit der Maus über eine Nachricht fährt, infos 
über die Nachricht und denn Spieler der sie gesendet hat.

#### send CMD
Durch den "send" Command können fake Nachrichten versendet werden und sind von einer Normalen nicht mehr zu
unterscheiden. Ein Spieler mit der Berechtigung "chat.fake" sieht das es eine Fake Nachricht ist und wenn
er über sie fährt wird der Tatsächliche Absender genannt. Für Spieler mit der Permission "chat.info" wird
wenn sie über eine Fake Nachricht fahren kein HoverEvent angezeigt dafür kann man ende des Befehls ein
JsonObject angegeben werden im format:
- name: name des Spielers
- leben: Leben des Spielers
- gamemode: Gamemode
- pos:
- - x: X
- - y: Y
- - z: Z
- ping: Ping
- saturation (oder s): Sätigung
- tode: Tode

Beispiel: "/send Fantamomo owner hallo mein name ist Fantamomo 
{name:"Fantamomo", leben:20, gamemode:"CREATIV", pos:{x:29,y:9,z:2009}, ping:10, s:20, tode:1}"
(Die Reinfolge des JsonObjects ist Egal)

Das würde als ausgabe "[Owner] Fantamomo: hallo mein name ist Fantamomo" erzeugen.

## Fanta-Mod
Zu diesem Plugin gibt es einem Mod (der ist nicht erforderlich und Spieler die ihn nicht
haben werden auch nicht gekickt).

### raw CMD
mit dem "raw" Command können nachrichten den Mod gesendet werden z.B.

"raw Fantamomo hud/item {id:0,item:"diamond",x:100,y:90}"

würde (wenn Fantamomo den Mod Installiert hat) auf dem Screen von Fantamomo das Item "diamond" an 
der position (100|90) anzeigen um das Item zu entfernen muss einfach item auf "" oder "air" gesetzt werden.
Wichtig die id muss immer gleich bleiben.

Andere werte für hud/item sind:
- hud/tooltip
- hud/text
- hud/fill
- hud/border
- hud/texture

x und y sind optionale paramenter
#### - hud/tooltip
Zeigt einen ItemTooltip an {id:0,text:["hallo","zeile2"]}
#### - hud/text
Zeigt Text an {id:0,text:"text",shadow:true,color:0xFF00FF}

### screen
Es gibt auch die Möglichkeit einen CustomScreen am Client zu öffnen. Mit "raw Fantamomo
screen/custom {title:"Title",widget:[{type:"textField",x:300,y:200,id:0},{type:"button",x:350,y:100,id:1}]}"
würde ein screen am client erzeugt werden, wo es eine Text Eingabe und einen Button gibt. Es gibt noch
type:checkbox, slider, togglebutton. Jedes dieser widgets unterstütst das Element tooltip:"das ist ein tooltip".

#### screen/optional
erzeugt einen Screen der aus einem Text (Frage) besteht und beliebig vielen Buttons drunter, z.B. 
"raw Fantamomo screen/optional {title:"Das ist der Title",buttons:["button name 1", "button2", "button3"]}"
Sollte der Spieler auf einen Button klicken wird der Screen geschlossen und ein Paket an den Server gesendet. Zur 
jetziger Zeit kann nur dieses Plugin die Daten lesen und sie in der Console ausgeben.

#### <b>Achtung</b> 
Die Screen funktionen sind da weil noch experimentel, man kann zwar Screens erzeugen, kann die daten aber noch nicht (richtig) 
abfragen.

### Placeholder
Es werde Placeholder hinzugefügt diese können in Join-, Quit-Message und sonstiges Zeug benutzt werden.
Hat ein Spieler die Berechtigung <b>"chat.placeholder.TabComplete"</b> so werden ihm, in Befehlen (nicht im CHAT,
da es nicht möglich ist, das zu bearbeiten) wenn er in "%" eingibt Placeholder vorgeschlagen. Es gibt welche
"%isVanish:[true]:[false]" bei den wenn der Spieler in dem Fall gevanished ist wird "[true]" ausgegeben, wenn nicht
"[false]" (Es wird das ausgegeben das zwischen dem ersten ':' und dem zweiten ':' ist mit dem klammern)

Um die Placeholder in befehlen zu nutzen wird die Permission <b>"chat.placeholder.format"</b> benötigt. Aber damit
die Placeholder ausgewertet werden können muss am ende des Commands " -p" stehen sollte das nicht so sein wird einfach
der Placeholder genommen nicht der Wert den er Representiert.
## Tabliste
Das Plugin ändert die Tabliste es wird der Ping, Rang und Online Player angezeigt für Spieler mit der Permission
"tablist.info" werde zusatz Information angezeigt wie Kordinaten und die Anzahl Vanish Players am server.


## Sonstiges
### Freeze
Es gibt auch einen Freeze command der Spieler Einfiert, diese Spieler können sich nicht bewegen, Items dropen,
Items aufheben und das Inventar bearbeiten.



<i>Sollten ihm Text oben irgentwelche Rechtschreibfehler auftreten Entschuldige ich diese. 
(Ich habe Legastenie)</i>
