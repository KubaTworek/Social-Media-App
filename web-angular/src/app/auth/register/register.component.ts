import {Component, OnDestroy} from '@angular/core';
import {NgForm} from "@angular/forms";
import {DataStorageService} from "../../shared/data-storage.service";
import {Subscription} from "rxjs";
import {AuthorizationService} from "../service/authorization.service";
import {RegisterRequest} from "../shared/register-request.type";

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {

  errorMessage: string = '';
  registerErrorSubscription: Subscription;
  loading: boolean = false;

  constructor(
    private authorizationService: AuthorizationService,
    private dataStorageService: DataStorageService
  ) {
    this.registerErrorSubscription = this.authorizationService.getRegisterError().subscribe(error => {
      this.errorMessage = error;
      this.loading = false;
    });
  }

  ngOnDestroy(): void {
    this.registerErrorSubscription.unsubscribe();
  }

  onSubmit(form: NgForm): void {
    if (!form.valid) {
      return;
    }

    const email = form.value.username;
    const password = form.value.password;
    const firstName = form.value['first-name'];
    const lastName = form.value['last-name'];

    const registerRequest: RegisterRequest = new RegisterRequest(
      email, password, firstName, lastName, 'ROLE_USER'
    );

    this.loading = true;

    this.dataStorageService.register(registerRequest).subscribe(
      () => {
        this.loading = false;
      },
      () => {
        this.loading = false;
      }
    );

    form.reset();
  }
}
