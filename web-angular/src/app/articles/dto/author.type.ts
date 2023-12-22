export class Author {
  public id: string;
  public username: string;
  public firstName: string;
  public lastName: string;

  constructor(
    id: string,
    username: string,
    firstName: string,
    lastName: string
  ) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
