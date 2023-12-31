export class Author {
  public id: string;
  public firstName: string;
  public lastName: string;
  public username: string;
  public following: number[];
  public followers: number[];

  constructor(id: string, firstName: string, lastName: string, username: string, following: number[], followers: number[]) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.following = following;
    this.followers = followers;
  }
}
