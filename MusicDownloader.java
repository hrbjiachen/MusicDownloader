import org.jsoup.Connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicDownloader{
    private static final String SONG_URL = "http://music.163.com/song";
    private static final String DOWNLOADER_URL = "https://ouo.us/fm/163/";
    public final File SONG_DIR = new File("./song/");

    private Connection get163Connection(String url) {
        return Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, sdch")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Cookie", "_ntes_nnid=7eced19b27ffae35dad3f8f2bf5885cd,1476521011210; _ntes_nuid=7eced19b27ffae35dad3f8f2bf5885cd; usertrack=c+5+hlgB7TgnsAmACnXtAg==; Province=025; City=025; _ga=GA1.2.1405085820.1476521280; NTES_PASSPORT=6n9ihXhbWKPi8yAqG.i2kETSCRa.ug06Txh8EMrrRsliVQXFV_orx5HffqhQjuGHkNQrLOIRLLotGohL9s10wcYSPiQfI2wiPacKlJ3nYAXgM; P_INFO=hourui93@163.com|1476523293|1|study|11&12|jis&1476511733&mail163#jis&320100#10#0#0|151889&0|g37_client_check&mailsettings&mail163&study&blog|hourui93@163.com; JSESSIONID-WYYY=189f31767098c3bd9d03d9b968c065daf43cbd4c1596732e4dcb471beafe2bf0605b85e969f92600064a977e0b64a24f0af7894ca898b696bd58ad5f39c8fce821ec2f81f826ea967215de4d10469e9bd672e75d25f116a9d309d360582a79620b250625859bc039161c78ab125a1e9bf5d291f6d4e4da30574ccd6bbab70b710e3f358f%3A1476594130342; _iuqxldmzr_=25; __utma=94650624.1038096298.1476521011.1476588849.1476592408.6; __utmb=94650624.11.10.1476592408; __utmc=94650624; __utmz=94650624.1476521011.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)")
                .header("DNT", "1")
                .header("Host", "music.163.com")
                .header("Pragma", "no-cache")
                .header("Referer", "http,//music.163.com/")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
    }

    public String getSongDownloadURL(String songID) throws IOException {
        Element body = Jsoup.connect(DOWNLOADER_URL)
                .data("id", songID)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, sdch")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Cookie", "_ntes_nnid=7eced19b27ffae35dad3f8f2bf5885cd,1476521011210; _ntes_nuid=7eced19b27ffae35dad3f8f2bf5885cd; usertrack=c+5+hlgB7TgnsAmACnXtAg==; Province=025; City=025; _ga=GA1.2.1405085820.1476521280; NTES_PASSPORT=6n9ihXhbWKPi8yAqG.i2kETSCRa.ug06Txh8EMrrRsliVQXFV_orx5HffqhQjuGHkNQrLOIRLLotGohL9s10wcYSPiQfI2wiPacKlJ3nYAXgM; P_INFO=hourui93@163.com|1476523293|1|study|11&12|jis&1476511733&mail163#jis&320100#10#0#0|151889&0|g37_client_check&mailsettings&mail163&study&blog|hourui93@163.com; JSESSIONID-WYYY=189f31767098c3bd9d03d9b968c065daf43cbd4c1596732e4dcb471beafe2bf0605b85e969f92600064a977e0b64a24f0af7894ca898b696bd58ad5f39c8fce821ec2f81f826ea967215de4d10469e9bd672e75d25f116a9d309d360582a79620b250625859bc039161c78ab125a1e9bf5d291f6d4e4da30574ccd6bbab70b710e3f358f%3A1476594130342; _iuqxldmzr_=25; __utma=94650624.1038096298.1476521011.1476588849.1476592408.6; __utmb=94650624.11.10.1476592408; __utmc=94650624; __utmz=94650624.1476521011.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)")
                .header("DNT", "1")
                .header("Host", "music.163.com")
                .header("Pragma", "no-cache")
                .header("Referer", "http,//music.163.com/")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
                .get().body();

        return body.select("a[class=button]").get(1).attr("href");
    }

    static final class ElementNotFoundException extends Exception {

        public ElementNotFoundException(String msg) {
            super(msg);
        }
        public ElementNotFoundException() {
            super();
        }
    }

    public String getSongID(String fileURL) {
        String songId = fileURL.trim();
        if (!songId.matches("^\\d*$")) {
            String tag = "song";
            String regex = tag + "\\?id=(\\d*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(songId);
            if (matcher.find())
                songId = matcher.group(1);
        }
        return songId;
    }


    public String getSongInfo(String fileURL) throws IOException, ElementNotFoundException {

        String songId = getSongID(fileURL);

        Element body = get163Connection(SONG_URL)
                .data("id", songId)
                .get().body();

        Element info = body.selectFirst("div[class=cnt]");
        if (info == null)
            throw new ElementNotFoundException("Unable to get song, id: " + songId);
        String songTitle = makeStringValidForWindowsFile(info.selectFirst("em[class=f-ff2]").text());
        Elements eleInfo = info.select("a[class=s-fc7]");
        Element eleArtist = eleInfo.get(0);
        String songArtist = makeStringValidForWindowsFile(eleArtist.text());

        return songArtist + "-" + songTitle;
    }

    public boolean downloadFile(String originURL)
            throws IOException, MalformedURLException {
        if (!SONG_DIR.exists())
            SONG_DIR.mkdir();

        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        String songInfo = "";
        try {
            String songID = getSongID(originURL);
            String downloadURL = getSongDownloadURL(songID);
            System.out.println(downloadURL);
            songInfo = getSongInfo(originURL);
            File file = new File("./song/", songInfo + ".mp3");
            URL website = new URL("http:"+ downloadURL);

            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Resource Unavailable");
        }catch (Exception e) {
            System.out.println("Error");
        }finally {
            try {
                rbc.close();
                fos.close();
            } catch (NullPointerException | IOException e) { }
        }

        Boolean status = false;

        if(new File("./song/",songInfo + ".mp3").exists() && !songInfo.isEmpty())
            status = true;

        return status;
    }

    public String makeStringValidForWindowsFile(String str) {
        return str
                .replace(':', '：')
                .replace('<', '＜')
                .replace('>', '＞')
                .replace('\"', '＂')
                .replace('/', '／')
                .replace('\\', '｜')
                .replace('?', '？')
                .replace('*', '＊');
    }


}