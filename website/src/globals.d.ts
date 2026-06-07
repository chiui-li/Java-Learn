declare namespace D {
  interface Result<T = any> {
    data?: T;
    code?: number;
    errMsg?: string;
  }

  interface User {
    username?: string;
    email?: string;
    password?: string;
  }

  type UserRes = Result<User>;

  type PostType = "article" | "images";

  type ArticleStatus = "draft" | "published";

  interface Article {
    id?: number;
    userId?: number;
    title?: string;
    content?: string;
    createdAt?: string;
    updatedAt?: string;
    viewCount?: number;
    postType?: PostType;
    draftContent?: string;
    draftTitle?: string | null;
    status?: ArticleStatus;
    draftUpdatedAt?: string;
  }

  type ArticleRes = Result<Article>;


  interface Page<T> {
    list?: T[];
    pageNum?: number;
    pageSize?: number;
    total?: number;
    pages?: number;
  }

  type ArticleListRes = Result<Page<Article>>;
}
