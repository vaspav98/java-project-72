package gg.jte.generated.ondemand;
import hexlet.code.dto.BasePage;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,6,6,32,32,32,36};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <section>\r\n      <div class=\"container-fluid bg-dark p-5\">\r\n        <div class=\"row\">\r\n          <div class=\"col-md-10 col-lg-8 mx-auto text-white\">\r\n            <h1 class=\"display-3 mb-0\">Анализатор страниц</h1>\r\n            <p class=\"lead\">Бесплатно проверяйте сайты на SEO пригодность</p>\r\n            <form action=\"/urls\" method=\"post\" class=\"rss-form text-body\">\r\n              <div class=\"row\">\r\n                <div class=\"col\">\r\n                  <div class=\"form-floating\">\r\n                    <input id=\"url-input\" autofocus type=\"text\" required name=\"url\" aria-label=\"url\"\r\n                           class=\"form-control w-100\" placeholder=\"ссылка RSS\" autocomplete=\"off\">\r\n                    <label for=\"url-input\">Ссылка</label>\r\n                  </div>\r\n                </div>\r\n                <div class=\"col-auto\">\r\n                  <button type=\"submit\" class=\"h-100 btn btn-lg btn-primary px-sm-5\">Проверить</button>\r\n                </div>\r\n              </div>\r\n            </form>\r\n            <p class=\"mt-2 mb-0 text-muted\">Пример: https://www.example.com</p>\r\n          </div>\r\n        </div>\r\n      </div>\r\n    </section>\r\n  ");
			}
		}, page);
		jteOutput.writeContent("\r\n\r\n\r\n\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
