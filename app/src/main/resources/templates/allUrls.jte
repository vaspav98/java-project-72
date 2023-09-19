@import hexlet.code.dto.UrlsPage
@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
        <div class="container-lg mt-5">
            <h1>Сайты</h1>

            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                    @for(var url : page.getUrls())
                        <tr>
                            <td>${url.getId()}</td>
                            <td>
                                <a href="/urls/${url.getId()}">${url.getName()}</a>
                            </td>
                            <td>
                                @for(var urlCheck : page.getUrlChecks())
                                    @if(urlCheck.getUrlId() == url.getId())
                                        ${urlCheck.getCreatedAt().toString()}
                                    @endif
                                @endfor
                            </td>
                            <td>
                                @for(var urlCheck : page.getUrlChecks())
                                    @if(urlCheck.getUrlId() == url.getId())
                                        ${urlCheck.getStatusCode()}
                                    @endif
                                @endfor
                            </td>
                        </tr>
                    @endfor
                </tbody>
            </table>

            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center mt-5">
                    <li class="page-item disabled">
                        <a class="page-link" href="/urls?page=0">Previous</a>
                    </li>
                    <li class="page-item active">
                        <a class="page-link" href="/urls?page=1">1</a>
                    </li>
                    <li class="page-item disabled">
                        <a class="page-link" href="/urls?page=2">Next</a>
                    </li>
                </ul>
            </nav>
        </div>
    `
)