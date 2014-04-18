(ns jet-infosystems-test.test.handler
  (:use clojure.test
        ring.mock.request
        jet-infosystems-test.handler))

(def example-rss-data)

;; (run-queries '(
;;                "http://blogs.yandex.ru/search.rss?text=java"
;;                "http://blogs.yandex.ru/search.rss?text=scala"
;;                "http://blogs.yandex.ru/search.rss?text=clojure"
;;                ;; "http://blogs.yandex.ru/search.rss?text=qwe"
;;                ))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))

(def example-rss-data
  {:cookies {"yandexuid"
             {:discard
              false, :domain ".yandex.ru", :expires
              #inst "2038-01-19T03:14:07.000-00:00", :path "/", :secure
              false, :value "6624122371397738972", :version
              0}}, :orig-content-encoding "gzip", :trace-redirects
   ["http://blogs.yandex.ru/search.rss?text=test"], :request-time 596, :status
   200, :headers {"X-RateLimit-3600-Reset" "1397741895", "Content-Type" "text/xml;
charset=utf-8", "Date" "Thu, 17 Apr 2014 12:49:32
GMT", "X-RateLimit-86400-Reset" "1397755117", "Cache-Control" "max-age=3600,
proxy-revalidate", "X-RateLimit-3600-Remaining" "65", "Expires" "Thu, 17 Apr
2014 13:49:32
GMT", "Transfer-Encoding" "chunked", "X-RateLimit-86400-Remaining" "4909", "Connection" "Close"}, :body "<?xml
version=\"1.0\" encoding=\"utf-8\"?>\n<rss xmlns:yablogs=\"urn:yandex-blogs\"
xmlns:wfw=\"http://wellformedweb.org/CommentAPI/\" version=\"2.0\">\n
<channel>\n <link>http://blogs.yandex.ru/search.xml?text=test</link>\n
<title>test — Яндекс.Поиск по блогам</title>\n <image>\n
<url>http://img.yandex.net/i/logo100x43.png</url>\n <title>Поиск Яндекса по
блогам</title>\n <link>http://blogs.yandex.ru/search.xml?text=test</link>\n
<width>100</width>\n <height>43</height>\n </image>\n <ttl>60</ttl>\n
<generator>blogs.yandex.ru</generator>\n
<webMaster>support@blogs.yandex.ru</webMaster>\n
<copyright>noindex</copyright>\n <description>Результаты поиска Яндекса по
блогам и форумам по запросу: «test»</description>\n
<yablogs:count>53530</yablogs:count>\n
<yablogs:more>http://blogs.yandex.ru/search.xml?text=test&amp;p=1</yablogs:more>\n
<item>\n <author>http://blogs.mail.ru/mail/aqva.74/</author>\n <yablogs:author
url=\"http://blogs.mail.ru/mail/aqva.74/\">Галина Евсеева -
Блоги@Mail.Ru</yablogs:author>\n <title>Re: тест на пошлость...</title>\n
<pubDate>Thu, 17 Apr 2014 12:28:53 GMT</pubDate>\n
<guid>http://my.mail.ru/community/man/7EF5E1B3F8E7BFFF.html?thread=4D1035805DC145E3</guid>\n
<link>http://my.mail.ru/community/man/7EF5E1B3F8E7BFFF.html?thread=4D1035805DC145E3</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Fmy.mail.ru%2Fcommunity%2Fman%2F7EF5E1B3F8E7BFFF.html%3Fthread%3D4D1035805DC145E3&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal url=\"http://blogs.mail.ru/community/man/\">Журнал сообщества
man</yablogs:journal>\n <yablogs:is-comment/>\n
<description>\n\nhttp://www.adme.ru/video/projdite-&lt;b&gt;test&lt;/b&gt;-na-poshlost-666205/</description>\n
</item>\n <item>\n <author>http://sea-mammal.livejournal.com/</author>\n
<yablogs:author
url=\"http://sea-mammal.livejournal.com/\">sea_mammal</yablogs:author>\n
<title>\n\nЭто называется Duck test.</title>\n <pubDate>Thu, 17 Apr 2014
11:53:40 GMT</pubDate>\n
<guid>http://gern-babushka13.livejournal.com/108383.html?thread=362847</guid>\n
<link>http://gern-babushka13.livejournal.com/108383.html?thread=362847</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Fgern-babushka13.livejournal.com%2F108383.html%3Fthread%3D362847&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://gern-babushka13.livejournal.com/\">gern_babushka13</yablogs:journal>\n
<yablogs:is-comment/>\n <description>\n\nЭто называется Duck
&lt;b&gt;test&lt;/b&gt;.</description>\n </item>\n <item>\n
<author>http://users.livejournal.com/</author>\n <yablogs:author
url=\"http://users.livejournal.com/\"/>\n <title>kdFYDbXargURWMsjE</title>\n
<pubDate>Thu, 17 Apr 2014 11:53:31 GMT</pubDate>\n
<guid>http://strategy-forex.livejournal.com/33138.html?thread=697970</guid>\n
<link>http://strategy-forex.livejournal.com/33138.html?thread=697970</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Fstrategy-forex.livejournal.com%2F33138.html%3Fthread%3D697970&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://strategy-forex.livejournal.com/\">strategy_forex</yablogs:journal>\n
<yablogs:is-comment/>\n <description>\n\ncompany
website (http://www.lakotafunds.org/quicklinks/xanaxonline/#ty) buy xanax online
no prescription with mastercard - xanax show up 5 panel drug
&lt;b&gt;test&lt;/b&gt;</description>\n </item>\n <item>\n
<author>http://friendfeed.com/uzixiem</author>\n <yablogs:author
url=\"http://friendfeed.com/uzixiem\">FriendFeed - uzixiem</yablogs:author>\n
<title>\n Простейший пример такого рода – обработка узлов в обратном порядке
документа. &amp;lt;xsl:if</title>\n <pubDate>Thu, 17 Apr 2014 11:43:00
GMT</pubDate>\n
<guid>http://friendfeed.com/uzixiem/73682e50/xsl-position#guid=7d742d03d1dec156f93bba8f59830890</guid>\n
<link>http://friendfeed.com/uzixiem/73682e50/xsl-position#guid=7d742d03d1dec156f93bba8f59830890</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Ffriendfeed.com%2Fuzixiem%2F73682e50%2Fxsl-position%23guid%3D7d742d03d1dec156f93bba8f59830890&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal url=\"http://friendfeed.com/uzixiem\">FriendFeed -
uzixiem</yablogs:journal>\n <yablogs:is-comment/>\n <description>\n\nПростейший
пример такого рода – обработка узлов в обратном порядке
документа. &amp;amp;lt;xsl:if &lt;b&gt;test&lt;/b&gt;=&amp;quot;position() mod 2
= 0&amp;quot;&amp;amp;gt;. &amp;amp;lt; xsl Вот пример — версию XSLT можно
проверить, вызвав. xsl:output method=&amp;quot;xml&amp;quot;. это не хорошо?
last() будет последний в оси элемент если я в примере вывожу значения position()
и lastспособ, но не такой хороший как пример выше, который к сожалению не
работает.</description>\n </item>\n <item>\n
<author>http://twitter.com/benellis1986</author>\n <yablogs:author
url=\"http://twitter.com/benellis1986\">benellis1986</yablogs:author>\n
<title>\n 20 years ago today Brian Lara scored 375, breaking the world record
for the highest</title>\n <pubDate>Thu, 17 Apr 2014 11:25:08 GMT</pubDate>\n
<guid>http://twitter.com/benellis1986/statuses/456755231981072384</guid>\n
<link>http://twitter.com/benellis1986/statuses/456755231981072384</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Ftwitter.com%2Fbenellis1986%2Fstatuses%2F456755231981072384&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://twitter.com/benellis1986\">benellis1986</yablogs:journal>\n
<description>RT &lt;ns0:user name=\"HistoricalSport\"
server=\"twitter.com\"&gt;@HistoricalSport&lt;/ns0:user&gt;: 20 years ago today
Brian Lara scored 375, breaking the world record for the highest score in
&lt;b&gt;Test&lt;/b&gt; Cricket &lt;a href=\"http://t.co/YkMNevcc8M\"
xhref=\"http://twitter.com/HistoricalSport/status/456751538862424064/photo/1\"&gt;pic.twitter.com/YkMNevcc8M&lt;/a&gt;</description>\n
</item>\n <item>\n <author>http://twitter.com/ItIsKeithWu</author>\n
<yablogs:author
url=\"http://twitter.com/ItIsKeithWu\">itiskeithwu</yablogs:author>\n
<title>\n\nUp and at it, after today's test, it's rest time.</title>\n
<pubDate>Thu, 17 Apr 2014 11:21:20 GMT</pubDate>\n
<guid>http://twitter.com/ItIsKeithWu/statuses/456754276514033664</guid>\n
<link>http://twitter.com/ItIsKeithWu/statuses/456754276514033664</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Ftwitter.com%2FItIsKeithWu%2Fstatuses%2F456754276514033664&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://twitter.com/ItIsKeithWu\">itiskeithwu</yablogs:journal>\n
<description>Up and at it, after today&amp;#39;s &lt;b&gt;test&lt;/b&gt;,
it&amp;#39;s rest time.</description>\n </item>\n <item>\n
<author>http://twitter.com/yousoff79</author>\n <yablogs:author
url=\"http://twitter.com/yousoff79\">yousoff79</yablogs:author>\n <title>\n\nRT
@FactsOfSchool: *Taking a test* B, B, B, B, B, B... HOLD UP...</title>\n
<pubDate>Thu, 17 Apr 2014 11:21:16 GMT</pubDate>\n
<guid>http://twitter.com/yousoff79/statuses/456754259988463616</guid>\n
<link>http://twitter.com/yousoff79/statuses/456754259988463616</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Ftwitter.com%2Fyousoff79%2Fstatuses%2F456754259988463616&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://twitter.com/yousoff79\">yousoff79</yablogs:journal>\n
<description>RT &lt;ns0:user name=\"FactsOfSchool\"
server=\"twitter.com\"&gt;@FactsOfSchool&lt;/ns0:user&gt;: *Taking a
&lt;b&gt;test&lt;/b&gt;* \n\nB, \nB, \nB, \nB, \nB, \nB...\n\nHOLD UP... One of
these has to be wrong...</description>\n </item>\n <item>\n
<author>http://twitter.com/Unicorn_Terry</author>\n <yablogs:author
url=\"http://twitter.com/Unicorn_Terry\">unicorn_terry</yablogs:author>\n
<title>\n\n100__suns ellegirl.ru/tests/na-kogo-...</title>\n <pubDate>Thu, 17
Apr 2014 11:18:40 GMT</pubDate>\n
<guid>http://twitter.com/Unicorn_Terry/statuses/456753604779446272</guid>\n
<link>http://twitter.com/Unicorn_Terry/statuses/456753604779446272</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Ftwitter.com%2FUnicorn_Terry%2Fstatuses%2F456753604779446272&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://twitter.com/Unicorn_Terry\">unicorn_terry</yablogs:journal>\n
<description>&lt;ns0:user name=\"100__suns\"
server=\"twitter.com\"&gt;@100__suns&lt;/ns0:user&gt; &lt;a
href=\"http://t.co/a4yo0pdxmC\"
xhref=\"http://www.ellegirl.ru/tests/na-kogo-iz-zvezd-v-tvittere-tyi-pohoja/\"&gt;ellegirl.ru/&lt;b&gt;tests&lt;/b&gt;/na-kogo-...&lt;/a&gt;\nВо
тут</description>\n </item>\n <item>\n
<author>http://vk.com/id220796879</author>\n <yablogs:author
url=\"http://vk.com/id220796879\">vk.com/id220796879</yablogs:author>\n <title>Я
Максим Горький http://www.socionika.info/test.html</title>\n <pubDate>Thu, 17
Apr 2014 11:18:30 GMT</pubDate>\n
<guid>http://vk.com/wall220796879_1144</guid>\n
<link>http://vk.com/wall220796879_1144</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Fvk.com%2Fwall220796879_1144&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://vk.com/id220796879\">vk.com/id220796879</yablogs:journal>\n
<description>\n\nhttp://www.socionika.info/&lt;b&gt;test&lt;/b&gt;.html</description>\n
</item>\n <item>\n <author>http://twitter.com/snejana_06</author>\n
<yablogs:author
url=\"http://twitter.com/snejana_06\">snejana_06</yablogs:author>\n
<title>\n\nFilosofie test Nr.</title>\n <pubDate>Thu, 17 Apr 2014 11:18:17
GMT</pubDate>\n
<guid>http://twitter.com/snejana_06/statuses/456753508423696384</guid>\n
<link>http://twitter.com/snejana_06/statuses/456753508423696384</link>\n
<wfw:commentRss>http://blogs.yandex.ru/search.rss?post=http%3A%2F%2Ftwitter.com%2Fsnejana_06%2Fstatuses%2F456753508423696384&amp;ft=comments</wfw:commentRss>\n
<yablogs:journal
url=\"http://twitter.com/snejana_06\">snejana_06</yablogs:journal>\n
<description>Filosofie &lt;b&gt;test&lt;/b&gt; Nr. 2 &lt;a
href=\"http://t.co/Tw4ctXitIR\"
xhref=\"http://conspecte.ro\"&gt;conspecte.ro&lt;/a&gt; via &lt;ns0:user
name=\"conspectero\"
server=\"twitter.com\"&gt;@conspectero&lt;/ns0:user&gt;</description>\n
</item>\n </channel>\n</rss>\n"})
