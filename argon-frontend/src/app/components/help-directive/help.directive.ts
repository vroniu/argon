import { HelpComponent } from './help.component';
import { Directive, TemplateRef, ViewContainerRef, ComponentFactoryResolver, Input } from '@angular/core';
import * as helpEntries from '../../../assets/help.json';

export interface HelpEntry {
  title: string;
  content: string[];
}

export interface HelpDirectiveOptions {
  helpKey: string;
  offsetTop?: string;
  offsetRight?: string;
}

@Directive({
  selector: '[argHelp]'
})
export class HelpDirective {
  defaultTopOffset = '-2.3rem';
  defaultRightOffset = '-0.2rem'
  helpComponentElement: HTMLElement;
  parentComponentElement: HTMLElement;

  constructor(
    private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) { }

  @Input() set argHelp(options: HelpDirectiveOptions) {
    this.viewContainer.createEmbeddedView(this.templateRef);
    const helpComponent = this.viewContainer.createComponent(this.componentFactoryResolver.resolveComponentFactory(HelpComponent));
    helpComponent.instance.help = (helpEntries as any)[options.helpKey];
    this.helpComponentElement = helpComponent.location.nativeElement as HTMLElement;
    this.parentComponentElement = helpComponent.location.nativeElement.parentElement as HTMLElement;
    this.helpComponentElement.style.position = 'absolute';
    this.helpComponentElement.style.top = options.offsetTop ? options.offsetTop : this.defaultTopOffset;
    this.helpComponentElement.style.right = options.offsetRight ? options.offsetRight : this.defaultRightOffset;
    this.parentComponentElement.style.position = 'relative';
  }

}
