export class Notification {
  public id: string;
  public name: string;
  public message: string;
  public content: string;
  public createAt: string;

  constructor(id: string, name: string, message: string, content: string, createAt: string) {
    this.id = id;
    this.name = name;
    this.message = message;
    this.content = content;
    this.createAt = createAt;
  }
}
