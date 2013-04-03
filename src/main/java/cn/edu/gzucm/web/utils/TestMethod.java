package cn.edu.gzucm.web.utils;

public class TestMethod {
    public TestMethod() {

        try {
            DownloadFileBean bean = new DownloadFileBean("http://downloads.mysql.com/docs/refman-5.1-en.pdf", "E:\\temp", "refman-5.1-en.pdf", 5);
            SiteFileFetch fileFetch = new SiteFileFetch(bean);
            fileFetch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new TestMethod();
    }
}