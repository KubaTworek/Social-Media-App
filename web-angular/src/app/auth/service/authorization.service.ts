import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";


@Injectable()
export class AuthorizationService {
  private loginErrorSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');
  private registerErrorSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor(
    private router: Router,
  ) {
    this.setupInteractionsListener();
  }

  getLoginError(): Observable<string> {
    return this.loginErrorSubject.asObservable();
  }

  getRegisterError(): Observable<string> {
    return this.registerErrorSubject.asObservable();
  }

  handleLogin(jwt: string | undefined) {
    if (jwt) {
      const logoutTime = new Date().getTime() + 10800000;
      this.storeSessionData(jwt, logoutTime);
      sessionStorage.setItem('jwt', jwt);
      this.router.navigate(['/home']);
    }
  }

  handleRegister() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.removeSessionData();
    this.router.navigate(['/login']);
  }

  handleLoginError(errorMessage: string) {
    this.loginErrorSubject.next(errorMessage);
  }

  handleRegisterError(errorMessage: string) {
    this.registerErrorSubject.next(errorMessage);
  }

  private storeSessionData(jwt: string, logoutTime: number) {
    sessionStorage.setItem('jwt', jwt);
    sessionStorage.setItem('logout_time', logoutTime.toString());
  }

  private removeSessionData() {
    sessionStorage.removeItem('jwt');
    sessionStorage.removeItem('logout_time');
  }

  private setupInteractionsListener() {
    document.addEventListener("mousemove", () => {
      if (this.isSessionExpired()) {
        this.logout();
      }
    });
  }

  private isSessionExpired() {
    const logoutTime = Number(sessionStorage.getItem('logout_time'));
    if (logoutTime !== 0) {
      return new Date().getTime() >= logoutTime;
    } else {
      return false;
    }
  }
}
