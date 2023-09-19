package gg.jte.generated.ondemand;
import hexlet.code.dto.UrlsPage;
public final class JteallUrlsGenerated {
	public static final String JTE_NAME = "allUrls.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,6,6,20,20,22,22,22,24,24,24,24,24,24,24,27,27,28,28,29,29,29,30,30,31,31,35,35,53,53,53};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n        <div class=\"container-lg mt-5\">\r\n            <h1>Сайты</h1>\r\n\r\n            <table class=\"table table-bordered table-hover mt-3\">\r\n                <thead>\r\n                <tr>\r\n                    <th class=\"col-1\">ID</th>\r\n                    <th>Имя</th>\r\n                    <th class=\"col-2\">Последняя проверка</th>\r\n                    <th class=\"col-1\">Код ответа</th>\r\n                </tr>\r\n                </thead>\r\n                <tbody>\r\n                    ");
				for (var url : page.getUrls()) {
					jteOutput.writeContent("\r\n                        <tr>\r\n                            <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("</td>\r\n                            <td>\r\n                                <a href=\"/urls/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(url.getId());
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a>\r\n                            </td>\r\n                            <td>\r\n                                ");
					for (var urlCheck : page.getUrlChecks()) {
						jteOutput.writeContent("\r\n                                    ");
						if (urlCheck.getUrlId() == url.getId()) {
							jteOutput.writeContent("\r\n                                        ");
							jteOutput.setContext("td", null);
							jteOutput.writeUserContent(urlCheck.getCreatedAt().toString());
							jteOutput.writeContent("\r\n                                    ");
						}
						jteOutput.writeContent("\r\n                                ");
					}
					jteOutput.writeContent("\r\n                            </td>\r\n                            <td></td>\r\n                        </tr>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n\r\n            <nav aria-label=\"Page navigation\">\r\n                <ul class=\"pagination justify-content-center mt-5\">\r\n                    <li class=\"page-item disabled\">\r\n                        <a class=\"page-link\" href=\"/urls?page=0\">Previous</a>\r\n                    </li>\r\n                    <li class=\"page-item active\">\r\n                        <a class=\"page-link\" href=\"/urls?page=1\">1</a>\r\n                    </li>\r\n                    <li class=\"page-item disabled\">\r\n                        <a class=\"page-link\" href=\"/urls?page=2\">Next</a>\r\n                    </li>\r\n                </ul>\r\n            </nav>\r\n        </div>\r\n    ");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
