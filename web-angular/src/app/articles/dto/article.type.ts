import {Like} from "./like.type";

export class Article {
  public author_firstName: string;
  public author_lastName: string;
  public author_username: string;
  public timestamp: Date;
  public text: string;
  public id: string;
  public numOfLikes: number;
  public elapsed: string;
  public likes: Like[];

  constructor(
    author_firstName: string,
    author_lastName: string,
    author_username: string,
    timestamp: Date,
    text: string,
    id: string,
    numOfLikes: number,
    elapsed: string
  ) {
    this.author_firstName = author_firstName;
    this.author_lastName = author_lastName;
    this.author_username = author_username;
    this.timestamp = timestamp;
    this.text = text;
    this.id = id;
    this.numOfLikes = numOfLikes;
    this.elapsed = elapsed;
    this.likes = [];
  }
}
