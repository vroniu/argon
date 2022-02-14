import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private static TOAST_OPTIONS: MatSnackBarConfig = {
    duration: 3000,
    horizontalPosition: 'right',
    verticalPosition: 'top'
  }

  constructor(private snackBar: MatSnackBar) { }

  addToast(message: string) {
    this.snackBar.open(message, '', ToastService.TOAST_OPTIONS);
  }

  addSuccessToast(message: string) {
    this.snackBar.open(message, '', {...ToastService.TOAST_OPTIONS, panelClass: 'toast-success'});
  }

  addErrorToast(message: string) {
    this.snackBar.open(message, '', {...ToastService.TOAST_OPTIONS, panelClass: 'toast-error'});
  }

  addInfoToast(message: string) {
    this.snackBar.open(message, '', {...ToastService.TOAST_OPTIONS, panelClass: 'toast-info'});
  }

}
