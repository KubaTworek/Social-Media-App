import {LikesInfo} from "./likes-info.type";
import {AuthorDto} from "./author.type";
import {Article} from "./article.type";

export class ArticleWithComments {
  public author: AuthorDto;
  public timestamp: Date;
  public text: string;
  public id: string;
  public elapsed: string;
  public createDate: string;
  public likes: LikesInfo;
  public numOfLikes: number;
  public numOfComments: number;
  public comments: Article[];

  constructor(
    author: AuthorDto,
    timestamp: Date,
    text: string,
    id: string,
    elapsed: string,
    createDate: string,
    likes: LikesInfo,
    numOfLikes: number,
    comments: Article[]
  ) {
    this.author = author;
    this.timestamp = timestamp;
    this.text = text;
    this.id = id;
    this.elapsed = elapsed;
    this.createDate = createDate;
    this.likes = likes;
    this.numOfLikes = numOfLikes;
    this.numOfComments = comments.length;
    this.comments = comments;
  }
}
